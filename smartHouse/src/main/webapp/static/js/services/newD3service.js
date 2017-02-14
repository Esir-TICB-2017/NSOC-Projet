/**
 * Created by Loulou on 14/02/2017.
 */
/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc').factory('newD3Service', () => {
    const constants = {};
    return {
        init: (data) => {
            const svg = d3.select(`svg#homeChart`);
            constants.width = parseInt(svg.style('width'), 0);
            constants.height = parseInt(svg.style('height'), 0);
            constants.widthMargin = 15;
            constants.parseTime = d3.timeParse('%b %d, %Y %X');
            constants.curve = d3.curveBasis;
            constants.xScale = d3.scaleTime()
                .rangeRound([-constants.widthMargin, constants.width + constants.widthMargin]);
            constants.yScale = d3.scaleLinear()
                .rangeRound([constants.height, 0]);
            constants.xAxis = d3.axisBottom(constants.xScale);
            constants.yAxis = d3.axisLeft(constants.yScale);
            constants.lineGen = d3.line()
                .x((d) => constants.xScale(d.date))
                .y((d) => constants.yScale(d.data))
                .curve(constants.curve);
            constants.areaGen = d3.area()
                .x((d) => constants.xScale(d.date))
                .y0(constants.height)
                .y1((d) => constants.yScale(d.data))
                .curve(constants.curve);

            data.forEach((d) => {
                d.date = constants.parseTime(d.date);
                d.data = d.data;
            });
            constants.xScale
                .domain([new Date(data[0].date), new Date(data[data.length - 1].date)]);
            constants.yScale
                .domain([0, d3.max(data, (d) => d.data)]);
            constants.yScale.domain();

            const line = svg.append('path')
                .attr("class", "line")
                .attr("d", constants.lineGen(data))
                .on('mouseover', () => {
                    console.log('ici');
                });
            /*
            var totalLength = line.node().getTotalLength();

            line
                .attr("stroke-dasharray", totalLength + " " + totalLength)
                .attr("stroke-dashoffset", totalLength)
                .transition()
                .duration(2000)
                .ease(d3.easeLinear)
                .attr("stroke-dashoffset", 0);
            */
            svg.append("path")
                .attr("class", "area")
                .style('opacity', 0)
                .transition()
                .duration(1000)
                .ease(d3.easeLinear)
                .style('opacity', 1)
                .attr("d", constants.areaGen(data));

            svg.append("g")
                .attr('class', 'xAxis')
                .attr("transform", "translate(0," + (constants.height - 30) + ")")
                .call(constants.xAxis);
            svg.append("g")
                .attr('class', 'yAxis')
                .attr("transform", "translate(60, -10)")
                .call(constants.yAxis);
        },
        update: (data) => {
            data.forEach((d) => {
                d.date = constants.parseTime(d.date);
                d.data = d.data;
            });

            constants.xScale
                .domain([new Date(data[0].date), new Date(data[data.length - 1].date)]);
            constants.yScale
                .domain([0, d3.max(data, (d) => d.data)]);

            console.log(constants.yScale.domain());

            var svg = d3.select("body").transition();

            svg.select(".line")   // change the line
                .duration(750)
                .attr("d", constants.lineGen(data));
            svg.select('.area')
                .attr("class", "area")
                .duration(750)
                .attr("d", constants.areaGen(data));
            svg.select(".xAxis") // change the x axis
                .duration(750)
                .call(constants.xAxis);
            svg.select(".yAxis") // change the y axis
                .duration(750)
                .call(constants.yAxis);
        },
        appendGradient: () => {
            d3.select('svg#homeChart').append("linearGradient")
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
                        opacity: "0.09"
                    },
                ])
                .enter().append("stop")
                .attr("offset", function (d) {
                    return d.offset;
                })
                .attr("stop-color", function (d) {
                    return d.color;
                })
                .attr("stop-opacity", function (d) {
                    return d.opacity;
                });
        }
    }
});
