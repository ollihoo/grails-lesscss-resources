import org.grails.plugin.resource.mapper.MapperPhase

/**
 * @author Paul Fairless
 *
 * Mapping file to compile .less files into .css files
 */
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.lesscss.LessCompiler
import org.lesscss.LessException

class LesscssResourceMapper implements GrailsApplicationAware {

    GrailsApplication grailsApplication

    def phase = MapperPhase.GENERATION // need to run early so that we don't miss out on all the good stuff

    static defaultIncludes = ['**/*.less']

    def map(resource, config) {
        File originalFile = resource.processedFile
        File input = getOriginalFileSystemFile(resource.sourceUrl)
        File target = generateCompiledFileFromOriginal(originalFile.absolutePath)

        if (log.debugEnabled) {
            log.debug "Compiling LESS file [${originalFile}] into [${target}], with compress [${grailsApplication.config.grails?.resources?.mappers?.lesscss?.compress}]"
        }

        try {
            compileLessFileIntoTargetFile(input, target)
        } catch (LessException e) {
            log.error("error compiling less file: ${originalFile}", e)
        }

        // Update mapping entry
        // We need to reference the new css file from now on
        resource.processedFile = target

        // Not sure if i really need these
        resource.sourceUrlExtension = 'css'
        resource.contentType = 'text/css'
        resource.tagAttributes?.rel = 'stylesheet'
        resource.updateActualUrlFromProcessedFile()

    }

    private void compileLessFileIntoTargetFile(File input, File target) {
        LessCompiler lessCompiler = new LessCompiler()
        lessCompiler.setCompress(grailsApplication.config.grails?.resources?.mappers?.lesscss?.compress == true ?: false)
        lessCompiler.compile input, target
    }

    private File generateCompiledFileFromOriginal(String absolutePathToFile) {
        new File(absolutePathToFile + '.css')
    }

    private File getOriginalFileSystemFile(String sourcePath) {
        grailsApplication.parentContext.getResource(sourcePath).file
    }
}
