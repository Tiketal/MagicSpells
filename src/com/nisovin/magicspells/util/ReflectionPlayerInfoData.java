package com.nisovin.magicspells.util;

import java.lang.reflect.Constructor;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_11_R1.EnumGamemode;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;

public class ReflectionPlayerInfoData {
	
	@SuppressWarnings("rawtypes")
	Class cls;
	@SuppressWarnings("rawtypes")
	Constructor constructor;
	
	Object obj;
	
	public ReflectionPlayerInfoData(GameProfile profile,
			int num, EnumGamemode mode, IChatBaseComponent icbc) {
		try {
			cls = Class.forName("net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo.PlayerInfoData");
			
			constructor = cls.getConstructor(GameProfile.class, Integer.class, EnumGamemode.class, IChatBaseComponent.class);
			
			obj = constructor.newInstance(profile, num, mode, icbc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Object getObject() {
		return obj;
	}
}
