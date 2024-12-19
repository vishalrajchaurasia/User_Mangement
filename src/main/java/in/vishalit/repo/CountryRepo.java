package in.vishalit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.vishalit.entities.CountryEntity;



public interface CountryRepo extends JpaRepository<CountryEntity, Integer> {

}
