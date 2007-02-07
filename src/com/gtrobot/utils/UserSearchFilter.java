package com.gtrobot.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import com.gtrobot.context.GlobalContext;
import com.gtrobot.context.UserEntry;

public class UserSearchFilter {
	private List userList;

	private int count;

	public UserSearchFilter(SortedSet sourceUserList, String condition) {
		userList = new ArrayList();
		if (condition == null || condition.trim().length() == 0) {
			copy(sourceUserList, userList);
		} else {
			filterCopy(sourceUserList, userList, condition.trim());
		}
		resetCount();
	}

	public String getUser(int pos) {
		return (String) userList.get(pos);
	}

	public int size() {
		return userList.size();
	}

	public void resetCount() {
		count = 0;
	}

	public int getCount() {
		return count;
	}

	public void storeCount(int count) {
		this.count = count;
	}

	private static void filterCopy(SortedSet sourceUserList,
			List targetUserList, String condition) {
		Iterator it = sourceUserList.iterator();
		condition = condition.toLowerCase();
		while (it.hasNext()) {
			String user = (String) it.next();
			UserEntry userEntry = GlobalContext.getInstance().getUser(user);
			int pos1 = user.toLowerCase().indexOf(condition);
			int pos2 = userEntry.getNickName().toLowerCase().indexOf(condition);
			if (pos1 != -1 || pos2 != -1) {
				targetUserList.add(user);
			}
		}
	}

	private static void copy(SortedSet sourceUserList, List targetUserList) {
		Iterator it = sourceUserList.iterator();
		while (it.hasNext()) {
			targetUserList.add(it.next());
		}
	}
}
