angular.module('nsoc')
    .controller('homeController', ($scope, $http, $location, websocketService, d3ChartService) => {
        $scope.tab = "general";

        $scope.changeTab = (newTab) => {
            $scope.tab = newTab;
        };

        $scope.signOut = function () {
            $scope.GoogleAuth.signOut().then(() => {
                $http({
                    method: 'GET',
                    url: '/logout'
                }).then(function success(res) {
                    console.log(res);
                    console.log('User signed out.');
                    $location.path('/login');
                }, function error(err) {
                    // TODO Force logout on server
                    console.log(err);
                    console.log('Please try to logout again');
                });
            });
        };

        websocketService.start('ws://127.0.0.1:8080/', (evt) => {
            var obj = JSON.parse(evt.data);
            $scope.$apply(() => {
                $scope[obj.key] = parseInt(obj.value);
            });
        });

        const now = parseInt(new Date().getTime() / 1000);

        $http({
            method: 'POST',
            url: '/data',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            params: {
                "startDate": "1485710831",
                "endDate": now.toString(),
                "sensorName": "temperature"
            }
        }).then(function success(res) {
            console.log(res)
            d3ChartService.draw(res.data, 'homeChart');
        }, function error(err) {
            console.log(err);
        });
    });

angular.module('nsoc').factory('websocketService', () => {
    return {
        start: function (url, callback) {
            var websocket = new WebSocket(url);
            websocket.onopen = () => {
                console.log("Opened!");
                websocket.send("Hello Server");
            };
            websocket.onclose = () => {
                console.log("Closed!");
            };
            websocket.onmessage = (evt) => {
                callback(evt);
            };
            websocket.onerror = (err) => {
                console.log("Error: " + err);
            };
        }
    }
});

angular.module('nsoc').factory('d3ChartService', () => {
    return {
        draw: (data, svgId) => {
            console.log(data, svgId)
           const svg = d3.select(`svg#${svgId}`);
           const width = parseInt(svg.style('width'), 0);
           const height = parseInt(svg.style('height'), 0);
           const parseTime = d3.timeParse('%b %d, %Y %X');
           const curve = d3.curveBasisOpen;
           const xScale = d3.scaleTime()
               .domain([new Date(data[0].date), new Date(data[data.length - 1].date)])
               .rangeRound([0, width]);
           const xAxis = d3.axisBottom(xScale);
           const yScale = d3.scaleLinear()
               .domain([0, d3.max(data, (d) => d.data)])
               .rangeRound([height, 0]);
           const yAxis = d3.axisLeft(yScale);
           const line = d3.line()
               .x((d) => xScale(d.date))
               .y((d) => yScale(d.data))
               .curve(curve);
           const area = d3.area()
               .x((d) => xScale(d.date))
               .y0(height)
               .y1((d) => yScale(d.data))
               .curve(curve);

            data.forEach((d) => {
               d.date = parseTime(d.date);
               d.data = d.data;
            });

            svg.append("linearGradient")
                .attr("id", "area-gradient")
                .attr("gradientUnits", "userSpaceOnUse")
                .attr("x1", '50%').attr("y1", '40%')
                .attr("x2", '50%').attr("y2", '84%')
                .selectAll("stop")
                .data([
                    {
                        offset: "0%",
                        color: "#FFFFFF",
                        opacity: "0.7"
                    },
                    {
                        offset: "100%",
                        color: "#FFFFFF",
                        opacity: "0"
                    },
                ])
                .enter().append("stop")
                .attr("offset", function(d) {
                    return d.offset;
                })
                .attr("stop-color", function(d) {
                    return d.color;
                })
                .attr("stop-opacity", function(d) {
                    return d.opacity;
                });

            svg.append('path')
                .data([data])
                .attr("class", "line")
                .attr("d", line);
            svg.append("path")
                .data([data])
                .attr("class", "area")
                .attr("d", area);

            svg.append("g")
                .attr("transform", "translate(0," + height + ")")
                .call(xAxis);
            svg.append("g")
                .call(yAxis);
        }
    }
});
