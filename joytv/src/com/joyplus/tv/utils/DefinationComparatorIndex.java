package com.joyplus.tv.utils;

import java.util.Comparator;

import com.joyplus.tv.entity.URLS_INDEX;

public class DefinationComparatorIndex implements Comparator {

		@Override
		public int compare(Object first, Object second) {
			// TODO Auto-generated method stub
			int first_name = ((URLS_INDEX) first).defination;
			int second_name = ((URLS_INDEX) second).defination;
			if (first_name - second_name < 0) {
				return -1;
			} else if(first_name - second_name > 0){
				return 1;
			} else{
				return 0;
			}
		}
	}