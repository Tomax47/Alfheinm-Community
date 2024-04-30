package com.alfheim.aflheim_community.model.user;

public enum State {

    /**
     Suspended = Locked
     Banned = Expired
     Confirmed = Account confirmed
     Blacklisted = Banned Forever!
     */
    CONFIRMED, NOT_CONFIRMED, SUSPENDED, BANNED, BLACKLISTED
}
