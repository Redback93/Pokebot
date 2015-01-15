package twitchpokedex.database.maps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Setting extends MapModel
{
	private static final long serialVersionUID = 1L;

	private String key;
	private String value;
}
