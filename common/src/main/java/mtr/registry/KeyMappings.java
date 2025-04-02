package mtr.registry;

import com.mojang.blaze3d.platform.InputConstants;
import mtr.Keys;
import mtr.RegistryClient;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyMappings {

	public static final KeyMapping LIFT_MENU = new KeyMapping("key.mtr.lift_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.mtr.keybinding");
	public static final KeyMapping TRAIN_ACCELERATE = new KeyMapping("key.mtr.train_accelerate", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UP, "category.mtr.keybinding");
	public static final KeyMapping TRAIN_BRAKE = new KeyMapping("key.mtr.train_brake", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_DOWN, "category.mtr.keybinding");
	public static final KeyMapping TRAIN_NEUTRAL = new KeyMapping("key.mtr.train_neutral", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT, "category.mtr.keybinding");
	public static final KeyMapping TRAIN_TOGGLE_DOORS = new KeyMapping("key.mtr.train_toggle_doors", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT, "category.mtr.keybinding");
	public static final KeyMapping DEBUG_1_NEGATIVE = new KeyMapping("key.mtr.debug_1_negative", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_4, "category.mtr.keybinding");
	public static final KeyMapping DEBUG_2_NEGATIVE = new KeyMapping("key.mtr.debug_2_negative", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_5, "category.mtr.keybinding");
	public static final KeyMapping DEBUG_3_NEGATIVE = new KeyMapping("key.mtr.debug_3_negative", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_6, "category.mtr.keybinding");
	public static final KeyMapping DEBUG_1_POSITIVE = new KeyMapping("key.mtr.debug_1_positive", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_7, "category.mtr.keybinding");
	public static final KeyMapping DEBUG_2_POSITIVE = new KeyMapping("key.mtr.debug_2_positive", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_8, "category.mtr.keybinding");
	public static final KeyMapping DEBUG_3_POSITIVE = new KeyMapping("key.mtr.debug_3_positive", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_9, "category.mtr.keybinding");
	public static final KeyMapping DEBUG_ROTATE_CATEGORY_NEGATIVE = new KeyMapping("key.mtr.debug_cycle_negative", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_SUBTRACT, "category.mtr.keybinding");
	public static final KeyMapping DEBUG_ROTATE_CATEGORY_POSITIVE = new KeyMapping("key.mtr.debug_cycle_positive", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_ADD, "category.mtr.keybinding");

	public static void registerClient() {
		RegistryClient.registerKeyBinding(KeyMappings.LIFT_MENU);

		if (!Keys.LIFTS_ONLY) {
			RegistryClient.registerKeyBinding(KeyMappings.TRAIN_ACCELERATE);
			RegistryClient.registerKeyBinding(KeyMappings.TRAIN_BRAKE);
			RegistryClient.registerKeyBinding(KeyMappings.TRAIN_NEUTRAL);
			RegistryClient.registerKeyBinding(KeyMappings.TRAIN_TOGGLE_DOORS);
			RegistryClient.registerKeyBinding(KeyMappings.DEBUG_1_NEGATIVE);
			RegistryClient.registerKeyBinding(KeyMappings.DEBUG_2_NEGATIVE);
			RegistryClient.registerKeyBinding(KeyMappings.DEBUG_3_NEGATIVE);
			RegistryClient.registerKeyBinding(KeyMappings.DEBUG_1_POSITIVE);
			RegistryClient.registerKeyBinding(KeyMappings.DEBUG_2_POSITIVE);
			RegistryClient.registerKeyBinding(KeyMappings.DEBUG_3_POSITIVE);
			RegistryClient.registerKeyBinding(KeyMappings.DEBUG_ROTATE_CATEGORY_NEGATIVE);
			RegistryClient.registerKeyBinding(KeyMappings.DEBUG_ROTATE_CATEGORY_POSITIVE);
		}
	}
}
