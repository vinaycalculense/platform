package calculense.platform.filemanager.service

import calculense.platform.auth.util.getRequestUser
import calculense.platform.auth.util.key
import calculense.platform.filemanager.dao.SampleRepository
import calculense.platform.filemanager.model.Sample
import calculense.platform.filemanager.util.FileUploadUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class SampleService:ISampleService {

    @Autowired
    private lateinit var sampleRepository: SampleRepository

    @Autowired
    private lateinit var fileUploadUtil: FileUploadUtil

    override fun saveSample(sample: Sample): Sample{
        return sampleRepository.save(sample)
    }

    override fun getSamples(category:String):List<Sample>{
        val samples = sampleRepository.findByCategoryAndUserIdIsNullOrUserId(category, getRequestUser().id!!)
        samples.forEach {
            it.sampleFiles.forEach { it1 ->
                it1.url= fileUploadUtil.generateGetUrl(bucket = it1.bucket, key=it1.key, duration = 300)
                it1.bucket=""
                it1.key=""
            }
        }
        return samples
    }
}