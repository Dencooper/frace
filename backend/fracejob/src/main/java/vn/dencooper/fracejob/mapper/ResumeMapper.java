package vn.dencooper.fracejob.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.dencooper.fracejob.domain.Resume;
import vn.dencooper.fracejob.domain.dto.response.resume.ResumeCreationResponse;
import vn.dencooper.fracejob.domain.dto.response.resume.ResumeFetchReponse;
import vn.dencooper.fracejob.domain.dto.response.resume.ResumeUpdationResponse;

@Mapper(componentModel = "spring")
public interface ResumeMapper {
    ResumeCreationResponse toResumeCreationResponse(Resume resume);

    ResumeUpdationResponse toResumeUpdationResponse(Resume resume);

    @Mapping(source = "user", target = "user", ignore = true)
    @Mapping(source = "job", target = "job", ignore = true)
    ResumeFetchReponse toResumeFetchReponse(Resume resume);
}
