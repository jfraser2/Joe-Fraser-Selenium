package springboot.entities;

import javax.persistence.*;

@Table(name = "url_short_name", schema = "public")
@Entity
public class UrlShortNameEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "url_id", columnDefinition = "int")
    private Long urlID;
	
    @Column(name = "url_long_name", columnDefinition = "VARCHAR(200)")
    private String urlLongName;
    
    @Column(name = "url_short_name", columnDefinition = "VARCHAR(200)", unique = true)
    private String urlShortName;

	public Long getUrlID() {
		return urlID;
	}

	public void setUrlID(Long urlID) {
		this.urlID = urlID;
	}

	public String getUrlLongName() {
		return urlLongName;
	}

	public void setUrlLongName(String urlLongName) {
		this.urlLongName = urlLongName;
	}

	public String getUrlShortName() {
		return urlShortName;
	}

	public void setUrlShortName(String urlShortName) {
		this.urlShortName = urlShortName;
	}

    
}
