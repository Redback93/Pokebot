package twitchpokedex.twitch.api;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class TwitchUser
{
	private String type;
	private String name;
	@SerializedName("created_at")
	private Date createdAt;
	@SerializedName("updated_at")
	private Date updatedAt;
	private String logo;
	@SerializedName("_id")
	private int id;
	@SerializedName("display_name")
	private String displayName;
	private String bio;
}
