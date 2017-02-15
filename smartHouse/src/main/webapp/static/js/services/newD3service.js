/**
 * Created by Loulou on 14/02/2017.
 */
/**
 * Created by loulou on 30/01/2017.
 */
angular.module('nsoc').factory('newD3Service', ($rootScope) => {
    const constants = {};
    const currentData = {};
    const tooltip = {
        xRect: -100,
        yRect: -100,
        rectWidth: 60,
        rectHeight: 40,
    };
    function mousemoved() {
        var pathLength = d3.select('.line').node().getTotalLength();
        var x = d3.event.pageX;
        var beginning = x, end = pathLength, target;
        while (true) {
            target = Math.floor((beginning + end) / 2);
            pos = d3.select('.line').node().getPointAtLength(target);
            if ((target === end || target === beginning) && pos.x !== x) {
                break;
            }
            if (pos.x > x)      end = target;
            else if (pos.x < x) beginning = target;
            else                break; //position found
        }
        const tooltipGroup = d3.select('g.tooltip')
            .attr('opacity', 1);
        tooltipGroup.select('circle').attr("cx", x).attr("cy", pos.y);
        let yTooltipRect = pos.y - 50;
        if (yTooltipRect < 0) {
            yTooltipRect = pos.y + 20;
        }
        const rect = tooltipGroup.select('rect')
            .attr('x', tooltip.xRect = x - tooltip.rectWidth / 2)
            .attr('y', tooltip.yRect = yTooltipRect);
        const text = tooltipGroup.select('text')
            .attr('x', tooltip.xRect + tooltip.rectWidth / 2)
            .attr('y', tooltip.yRect + tooltip.rectHeight / 2)
            .text(constants.yScale.invert(pos.y).toFixed(1));

        rect.attr('width', tooltip.rectWidth = text.node().getBBox().width + 10)
        rect.attr('x', tooltip.xRect = x - tooltip.rectWidth / 2);
    }

    function closestPoint(pathNode, point) {
        console.log(pathNode.getPointAtLength())
        var pathLength = pathNode.getTotalLength(),
            precision = 8,
            best,
            bestLength,
            bestDistance = Infinity;

        // linear scan for coarse approximation
        for (var scan, scanLength = 0, scanDistance; scanLength <= pathLength; scanLength += precision) {
            if ((scanDistance = distance2(scan = pathNode.getPointAtLength(scanLength))) < bestDistance) {
                best = scan, bestLength = scanLength, bestDistance = scanDistance;
            }
        }

        // binary search for precise estimate
        precision /= 2;
        while (precision > 0.5) {
            var before,
                after,
                beforeLength,
                afterLength,
                beforeDistance,
                afterDistance;
            if ((beforeLength = bestLength - precision) >= 0 && (beforeDistance = distance2(before = pathNode.getPointAtLength(beforeLength))) < bestDistance) {
                best = before, bestLength = beforeLength, bestDistance = beforeDistance;
            } else if ((afterLength = bestLength + precision) <= pathLength && (afterDistance = distance2(after = pathNode.getPointAtLength(afterLength))) < bestDistance) {
                best = after, bestLength = afterLength, bestDistance = afterDistance;
            } else {
                precision /= 2;
            }
        }

        best = [best.x, best.y];
        best.distance = Math.sqrt(bestDistance);
        return best;

        function distance2(p) {
            var dx = p.x - point[0],
                dy = p.y - point[1];
            return dx * dx + dy * dy;
        }
    }

    return {
        init: () => {

            const svg = d3.select(`svg#homeChart`).on("mousemove", mousemoved);
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


            constants.xScale
                .domain([new Date(currentData.data[0].date), new Date(currentData.data[currentData.data.length - 1].date)]);
            constants.yScale
                .domain([0, d3.max(currentData.data, (d) => d.data)]);
            constants.yScale.domain();

            const line = svg.append('path')
                .attr("class", "line")
                .attr("d", constants.lineGen(currentData.data))

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
                .attr("d", constants.areaGen(currentData.data));


            svg.append("g")
                .attr('class', 'xAxis')
                .attr("transform", "translate(0," + (constants.height - 30) + ")")
                .call(constants.xAxis);
            svg.append("g")
                .attr('class', 'yAxis')
                .attr("transform", "translate(60, 0)")
                .call(constants.yAxis);

            const tooltipGroup = svg.append('g')
                .attr('class', 'tooltip');


            tooltipGroup.append("circle")
                .attr("cx", -10)
                .attr("cy", -10)
                .attr("r", 3.5)
                .attr('fill', 'white');

            tooltipGroup.append('rect')
                .attr('x', tooltip.xRect)
                .attr('y', tooltip.yRect)
                .attr('width', tooltip.rectWidth)
                .attr('height', tooltip.rectHeight)
                .attr('fill', 'white')
                .attr('rx', 4)
                .attr('ry', 4);

            tooltipGroup.append('text')
                .attr('dy', '.35em')
                .attr('fill', 'red')
                .style('text-anchor', 'middle')
                .style('font-size', 15);

            svg.append('text')
                .attr('x', constants.width /2)
                .attr('y', constants.height /2)
                .text($rootScope.unit)
                .attr('dy', '.35em')
                .attr('fill', 'red')
                .style('text-anchor', 'middle')
                .style('font-size', 15)
        },
        update: () => {

            d3.select('g.tooltip').attr('opacity', 0);
            constants.xScale
                .domain([new Date(currentData.data[0].date), new Date(currentData.data[currentData.data.length - 1].date)]);
            constants.yScale
                .domain([0, d3.max(currentData.data, (d) => d.data)]);


            var svg = d3.select("body").transition();

            svg.select(".line")   // change the line
                .duration(750)
                .attr("d", constants.lineGen(currentData.data));
            svg.select('.area')
                .attr("class", "area")
                .duration(750)
                .attr("d", constants.areaGen(currentData.data));
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
        },
        addRowToCurrentSet(row) {
            currentData.data.push(row);
        },
        updateCurrentData(data) {
            currentData.type = data[0].type;
            currentData.name = data[0].name;
            currentData.data = data;
        },
        getCurrentData: () => {
            return currentData;
        }
    }
});
