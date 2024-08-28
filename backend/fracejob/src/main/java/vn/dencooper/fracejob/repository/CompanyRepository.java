package vn.dencooper.fracejob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.dencooper.fracejob.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
