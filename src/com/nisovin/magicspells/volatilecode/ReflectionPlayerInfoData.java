package com.nisovin.magicspells.volatilecode;

import java.lang.reflect.Constructor;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_11_R1.EnumGamemode;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;

public class ReflectionPlayerInfoData {

	Class<?> outCls;
	Class<?> inCls;
	
	Constructor<?> inConstructor;
	
	Object outObj;
	Object inObj;
	
	public ReflectionPlayerInfoData(GameProfile profile, int num, EnumGamemode mode, IChatBaseComponent icbc) {
		
		try {
			outCls = Class.forName("net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo");
			outObj = outCls.newInstance();
			
			inCls = Class.forName("net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo$PlayerInfoData");
			inConstructor = inCls.getConstructors()[0];
			
			inObj = inConstructor.newInstance(outObj, profile, num, mode, icbc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Object getOuterObject() {
		return outObj;
	}
	
	public Object getInnerObject() {
		return inObj;
	}
}
