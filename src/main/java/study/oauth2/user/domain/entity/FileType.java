package study.oauth2.user.domain.entity;

public enum FileType {
	IMAGE("image"),
	SOUND("sound");

	private final String directory;

	FileType(String directory) {
		this.directory = directory;
	}

	public String getDirectory() {
		return directory;
	}
}
