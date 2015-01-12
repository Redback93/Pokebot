package twitchpokedex.database.maps;

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
}
