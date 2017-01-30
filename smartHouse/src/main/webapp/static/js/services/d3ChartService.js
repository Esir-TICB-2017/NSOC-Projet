/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc').factory('d3ChartService', () => {
    return {
        draw: (data, svgId) => {

            const svg = d3.select(`svg#${svgId}`);

            const width = parseInt(svg.style('width'), 0);
            const height = parseInt(svg.style('height'), 0);
            const widthMargin = 15;
            const parseTime = d3.timeParse('%b %d, %Y %X');
            const curve = d3.curveBasisOpen;
            const xScale = d3.scaleTime()
                .domain([new Date(data[0].date), new Date(data[data.length - 1].date)])
                .rangeRound([-widthMargin, width + widthMargin]);
            const xAxis = d3.axisBottom(xScale)
            const yScale = d3.scaleLinear()
                .domain([0, d3.max(data, (d) => d.data)])
                .rangeRound([height, 0]);
            const yAxis = d3.axisLeft(yScale);
            const lineGen = d3.line()
                .x((d) => xScale(d.date))
                .y((d) => yScale(d.data))
                .curve(curve);
            const areaGen = d3.area()
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
                    },
                    {
                        offset: "100%",
                        color: "#FFFFFF",
                        opacity: "0.09"
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

            const line = svg.selectAll('path.line').data([data]);
            const area = svg.selectAll('path.area').data([data]);

            line.enter().append('path')
                .attr("class", "line")
                .attr("d", lineGen);

            area.enter().append("path")
                .attr("class", "area")
                .attr("d", areaGen);


            /*
            svg.append("g")
                .attr("transform", "translate(0,0)")
                .call(xAxis);
            svg.append("g")
                .call(yAxis);
                */
            line.transition()
                .ease(d3.easeLinear)
                .duration(500)
                .attr("d", lineGen);

            area.transition()
                .ease(d3.easeLinear)
                .duration(500)
                .attr("d", areaGen);



        }
    }
});