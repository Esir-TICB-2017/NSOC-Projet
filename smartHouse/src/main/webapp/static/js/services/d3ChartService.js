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
            const bisectDate = d3.bisector(function(d) { return d.date; }).left;
            const curve = d3.curveBasis;
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
                        opacity: "0.7"
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

            const test = line.enter().append('path')
                .attr("class", "line")
                .attr("d", lineGen);
            area.enter().append("path")
                .attr("class", "area")
                .style('opacity', 0)
                .transition()
                .duration(1000)
                .ease(d3.easeLinear)
                .style('opacity', 1)
                .attr("d", areaGen);
            /*
            var totalLength = test.node().getTotalLength();

            test
                .attr("stroke-dasharray", totalLength + " " + totalLength)
                .attr("stroke-dashoffset", totalLength)
                .transition()
                .duration(2000)
                .ease(d3.easeLinear)
                .attr("stroke-dashoffset", 0);
                */


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

            const focus = svg.append("g")
                .attr("class", "focus")
                .style("display", "none");

            // append the circle at the intersection
            focus.append("circle")
                .style("fill", "#93E6FF")
                .style("stroke", "white")
                .style("stroke-width", 3)
                .attr("r", 6);

            const rect = focus.append("rect")
                .attr('width', 40)
                .attr('height', 30)
                .attr('y', -40)
                .attr('x', -20)
                .attr('rx', 5)
                .attr('ry', 5)
                .style("fill", "white");

            focus.append("text")
                .attr("x", 9)
                .attr("dy", ".35em");

            svg.append("rect")
                .attr('class', 'overlay')
                .attr("width", width)
                .attr("height", height)
                .attr('fill', 'none')
                .attr('pointer-events', 'all')
                .on("mouseover", function() { focus.style("display", null); })
                .on("mouseout", function() { focus.style("display", "none"); })
                .on("mousemove", mousemove);

            function mousemove() {
                var x0 = xScale.invert(d3.mouse(this)[0]),
                    i = bisectDate(data, x0, 1),
                    d0 = data[i - 1],
                    d1 = data[i],
                    d = x0 - d0.date > d1.date - x0 ? d1 : d0;
                focus.attr("transform", "translate(" + xScale(d.date) + "," + yScale(d.data) + ")");
                focus.select("text").text(parseInt(d.data));
            }



        }
    }
});
