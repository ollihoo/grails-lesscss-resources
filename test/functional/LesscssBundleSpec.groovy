import geb.spock.GebReportingSpec
import spock.lang.Stepwise

/**
 * @author Paul Fairless
 */
@Stepwise
class LesscssBundleSpec extends GebReportingSpec {

    def "check that all test elements are available" () {
        when:
        go('http://localhost:8080/lesscss-resources')

        then:
        $('h1').text() == 'Less Test'
        $('h2').text() == 'Imported Color'
        $('h3').text() == 'Another imported Color'
    }

//    def "check less-css rules rendered"() {
//        when:
//        go('http://localhost:8080/lesscss-resources')
//
//        then:
//        $('h1').css('color') == 'rgb(34, 34, 251)'
//        $('h2').css('color') == 'rgb(132, 34, 16)'
//        $('h3').css('color') == 'rgb(34, 251, 34)'
//
//        and: 'css processor still runs'
//        $('h1').jquery.css('margin-top') == '10px'
//    }

}