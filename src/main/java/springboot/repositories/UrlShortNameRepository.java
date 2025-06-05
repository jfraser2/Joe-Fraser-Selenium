package springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import springboot.entities.UrlShortNameEntity;

@Repository
public interface UrlShortNameRepository extends JpaRepository<UrlShortNameEntity, Long>{
	
    @Query(value = "SELECT entity FROM UrlShortNameEntity entity WHERE entity.urlShortName = :urlShortName")
    UrlShortNameEntity findByUrlShortName(@Param("urlShortName") String urlShortName);
    
    @Query(value = "SELECT entity FROM UrlShortNameEntity entity WHERE entity.urlLongName = :urlLongName")
    UrlShortNameEntity findByUrlLongName(@Param("urlLongName") String urlLongtName);

}
