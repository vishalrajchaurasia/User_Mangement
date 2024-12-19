package in.vishalit.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.vishalit.entities.CityEntity;


public interface CityRepo extends JpaRepository<CityEntity, Integer>{
	
	
	public List<CityEntity> findByStateId(Integer stateId);
}
