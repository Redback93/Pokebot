package twitchpokedex.database.maps;

import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Type extends MapModel
{
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private String name;
}
