package com.abnull.yrs.event.handler.client;

import org.lwjgl.input.Keyboard;

import com.abnull.yrs.customgui.CustomGuiSkillTree;
import com.abnull.yrs.proxy.ClientProxy;
import com.abnull.yrs.util.SkillData;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class GameKeyInputEventHandler {

	public static final KeyBinding[] skill_slots_key = new KeyBinding[6];
	public static final KeyBinding[] potion_slots_key = new KeyBinding[3];
	
	public GameKeyInputEventHandler()
	{
		skill_slots_key[0] = new KeyBinding("Skill Slot Key 1", Keyboard.KEY_R, "YRS Mod - GameKey");
		skill_slots_key[1] = new KeyBinding("Skill Slot Key 2", Keyboard.KEY_T, "YRS Mod - GameKey");
		skill_slots_key[2] = new KeyBinding("Skill Slot Key 3", Keyboard.KEY_Y, "YRS Mod - GameKey");
		skill_slots_key[3] = new KeyBinding("Skill Slot Key 4", Keyboard.KEY_F, "YRS Mod - GameKey");
		skill_slots_key[4] = new KeyBinding("Skill Slot Key 5", Keyboard.KEY_G, "YRS Mod - GameKey");
		skill_slots_key[5] = new KeyBinding("Skill Slot Key 6", Keyboard.KEY_H, "YRS Mod - GameKey");
		
		potion_slots_key[0] = new KeyBinding("Potion Slot Key 1", Keyboard.KEY_C, "YRS Mod - GameKey");
		potion_slots_key[1] = new KeyBinding("Potion Slot Key 2", Keyboard.KEY_V, "YRS Mod - GameKey");
		potion_slots_key[2] = new KeyBinding("Potion Slot Key 3", Keyboard.KEY_B, "YRS Mod - GameKey");
		
		for(int n = 0; n < skill_slots_key.length; n++)
		{
			ClientRegistry.registerKeyBinding(skill_slots_key[n]);
		}
		
		for(int n = 0; n < potion_slots_key.length; n++)
		{
			ClientRegistry.registerKeyBinding(potion_slots_key[n]);
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event)
	{
		for(int n = 0; n < skill_slots_key.length; n++)
		{
			if(skill_slots_key[n].getIsKeyPressed())
			{
				if(CustomGuiSkillTree.skill_slot_array[n] != null)
				{
					if(CustomGuiSkillTree.skill_slot_array[n].is_skill_exist())
					{
						String skill_name = CustomGuiSkillTree.skill_slot_array[n].get_skill_name();
						SkillData skill_data = ClientProxy.skill_data_map.get(skill_name);
						if(skill_data.has_skill_permission() && !skill_data.is_cooltiming())
						{
							int skill_cost_mp = skill_data.cost_mana;
							int player_mp = Integer.parseInt(ClientProxy.player_data.getString("mp"));
							if(player_mp < skill_cost_mp)
							{
								return;
							}
							ClientProxy.client_sending_data_manager.send_data_to_server("use_skill", new String[][]{ { "skill_slot_num", "" + n } });
							ClientProxy.skill_cooltime_map.remove(skill_name);
							ClientProxy.skill_cooltime_map.put(skill_name, System.currentTimeMillis());
							// ClientProxy.skill_cooltime_map.replace(skill_name, System.currentTimeMillis());
						}
					}
				}
			}
		}
		
		for(int n = 0; n < potion_slots_key.length; n++)
		{
			if(potion_slots_key[n].getIsKeyPressed())
			{
				NBTTagCompound potion_data = ClientProxy.player_data.getCompoundTag("potion_tree_data").getCompoundTag("" + n);
				boolean is_null = potion_data.getBoolean("is_null");
				if(!is_null)
				{
					ClientProxy.client_sending_data_manager.send_data_to_server("use_potion", new String[][]{ { "potion_slot_num", "" + n } });	
				}
			}
		}
	}
}
