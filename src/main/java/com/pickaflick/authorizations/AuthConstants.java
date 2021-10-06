package com.pickaflick.authorizations;

public class AuthConstants {
	  public static final String SECRET = "nYknijkL7uD006Fvbcjlj5Xq08xCA09x";
	  public static final long EXPIRATION_TIME = 432_000_000; // 5 days
	  public static final String TOKEN_PREFIX = "Bearer "; //we used Bearer because it was in our project. We could have used a JSON Web Token or a number of other options.
	  public static final String HEADER_STRING = "Authorization";
	}