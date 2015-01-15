package twitchpokedex.database.maps;

import java.util.Set;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;


@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Type extends MapModel
{
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private String name;
}
