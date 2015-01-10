package twitchpokedex.database.maps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;;

// Generated 08/01/2015 2:48:06 PM by Hibernate Tools 3.4.0.CR1

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Setting implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String value;
}
