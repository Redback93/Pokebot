package twitchpokedex.database.maps;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Pokemon extends MapModel
{
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private boolean legendary;
	@ManyToOne(cascade=CascadeType.ALL)
	private Type type1;
	@ManyToOne(cascade=CascadeType.ALL)
	private Type type2;
}
