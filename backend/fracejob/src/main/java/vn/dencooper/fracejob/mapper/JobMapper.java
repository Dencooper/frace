package vn.dencooper.fracejob.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.dencooper.fracejob.domain.Job;
import vn.dencooper.fracejob.domain.dto.response.job.JobResponse;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(source = "skills", target = "skills", ignore = true)
    JobResponse toJobResponse(Job job);
}
