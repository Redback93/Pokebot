package twitchpokedex.database.maps;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class InventoryItem extends MapModel
{
	private static final long serialVersionUID = 1L;

	private int id;
	private User user;
	private String identifier;
	private int quantity;
}
