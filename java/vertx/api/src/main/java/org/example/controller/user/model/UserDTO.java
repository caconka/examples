package org.example.controller.user.model;

public class UserDTO {

	private final String id;
	private final String username;
	private final String firstName;
	private final String lastName;
	private final String email;
	private final String phone;

	private UserDTO(Builder builder) {
		id = builder.id;
		username = builder.username;
		firstName = builder.firstName;
		lastName = builder.lastName;
		email = builder.email;
		phone = builder.phone;
	}

	public static Builder builder() {
		return new Builder();
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public static class Builder {

		private String id;
		private String username;
		private String firstName;
		private String lastName;
		private String email;
		private String phone;

		public UserDTO build() {
			return new UserDTO(this);
		}

		public Builder setId(String id) {
			this.id = id;
			return this;
		}

		public Builder setUsername(String username) {
			this.username = username;
			return this;
		}

		public Builder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder setEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder setPhone(String phone) {
			this.phone = phone;
			return this;
		}

	}

}
