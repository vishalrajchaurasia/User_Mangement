package in.vishalit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.vishalit.entities.StateEntity;



public interface StateRepo extends JpaRepository<StateEntity, Integer>{
	
	public List<StateEntity> findByCountryId(Integer countryId);
}
