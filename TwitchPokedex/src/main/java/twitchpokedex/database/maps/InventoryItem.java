package twitchpokedex.database.maps;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class InventoryItem extends MapModel
{
	private static final long serialVersionUID = 1L;

	private int id;
	private User user;
	private String identifier;
	private int quantity;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timestamp", nullable = false, columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
	private Date addedAt;
}
