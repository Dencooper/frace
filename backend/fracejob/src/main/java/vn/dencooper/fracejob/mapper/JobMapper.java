package vn.dencooper.fracejob.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.dencooper.fracejob.domain.Job;
import vn.dencooper.fracejob.domain.dto.response.job.JobResponse;

@Mapper(componentModel = "spring")
public interface JobMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void toJob(@MappingTarget Job job, Job request);

    @Mapping(source = "skills", target = "skills", ignore = true)
    JobResponse toJobResponse(Job job);
}
