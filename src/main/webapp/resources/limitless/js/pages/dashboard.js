jQuery(document).ready(function() {
    rc();
    rc2();
});

function loadGraphicCities(data,names) {
    $('#container2').highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: 'Şehir İstatistikleri'
        },

        xAxis: {
            type: 'category',
            labels: {
                rotation: -45,
                style: {
                    fontSize: '13px',
                    fontFamily: 'Verdana, sans-serif'
                }
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Oturum'
            }
        },
        legend: {
            enabled: false
        },
        dataLabels: {
                enabled: true,

                color: '#FFFFFF',
                align: 'right',
                format: '{point.y:.1f}', // one decimal
                y: 10, // 10 pixels down from the top
                style: {
                    fontSize: '13px',
                    fontFamily: 'Verdana, sans-serif'
                }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },
        series: [{
            name: 'Oturum Sayısı',
            data: data
         }]
});
}


function loadGraphicSessions(data) {
        $('#container').highcharts({
            chart: {
                zoomType: 'x',
                height:300
            },
            title: {
                text: 'Oturum Grafiği'
            },
            xAxis: {

            },
            yAxis: {
                title: {
                    text: 'Oturumlar'
                }
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                area: {
                    fillColor: {
                        linearGradient: {
                            x1: 0,
                            y1: 0,
                            x2: 0,
                            y2: 1
                        },
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    marker: {
                        radius: 2
                    },
                    lineWidth: 1,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    },
                    threshold: null
                }
            },

            series: [{
                type: 'area',
                name: 'Oturum Sayısı',
                data: data
            }]
        });
}
