package me.G4meM0ment.GSkills;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.skill.SkillConfigManager;
import com.herocraftonline.heroes.characters.skill.SkillSetting;
import com.herocraftonline.heroes.characters.skill.SkillType;
import com.herocraftonline.heroes.characters.skill.TargettedSkill;

public class SkillHarmtouch extends TargettedSkill {

	public SkillHarmtouch(Heroes plugin) 
    {
        super(plugin, "Harmtouch");
        setUsage("/skill harmtouch");
        setDescription("Your magical force drains live from your target");
        setTypes(SkillType.HARMFUL);
        setArgumentRange(0, 0);
        setIdentifiers("skill harmtouch");
    }

    @Override
    public String getDescription(Hero hero) 
    {
    	
    	double distance = ((SkillConfigManager.getUseSetting(hero, this, SkillSetting.RADIUS.node(), 3, false)));
    	int baseDamage = (int) ((SkillConfigManager.getUseSetting(hero, this, "BaseDamage", 6, false)) +
    			((SkillConfigManager.getUseSetting(hero, this, "LevelMultiplier", 0.1, false)) * hero.getSkillLevel(this)));
    	
        String description = getDescription().replace("$1", distance + "").replace("$2", baseDamage + "");
        return description;
    }

    @Override
    public ConfigurationSection getDefaultConfig() 
    {
        ConfigurationSection node = super.getDefaultConfig();
        node.set(SkillSetting.RADIUS.node(), 3);
        node.set("BaseDamage", 6);
        node.set("LevelMultiplier", 0.1);
        return node;
    }
    
	@Override
    public SkillResult use(Hero hero, LivingEntity target, String args[]) 
	{
    	Player p = hero.getPlayer();
    	int radius = SkillConfigManager.getUseSetting(hero, this, SkillSetting.RADIUS.node(), 3, false);
        double damage = ((SkillConfigManager.getUseSetting(hero, this, "BaseDamage", 6, false)) +
    			((SkillConfigManager.getUseSetting(hero, this, "LevelMultiplier", 0.1, false)) * hero.getSkillLevel(this)));
        
    	if(target == null || p == null) return SkillResult.CANCELLED;
    	if(p.getLocation().distance(target.getLocation()) > radius) return SkillResult.CANCELLED;
    	if(!damageCheck(p, target)) return SkillResult.CANCELLED;
    	Material m = p.getItemInHand().getType();
    	if(m != Material.ENCHANTED_BOOK)
    	{
    		p.sendMessage(ChatColor.GRAY+"Du musst ein Magierbuch tragen um disen Zauber zu wirken!");
    		return SkillResult.CANCELLED;
    	}
    		
    	broadcastExecuteText(hero, target);
        hero.setMana(hero.getMana() - (SkillConfigManager.getUseSetting(hero, this, "mana", 0, false)));
        damageEntity(target, p, damage, DamageCause.MAGIC);
        p.getWorld().playEffect(target.getLocation(), Effect.ENDER_SIGNAL, 0);
        p.getWorld().playSound(target.getLocation(), Sound.FIZZ, 10, 1);
	    	    	
        return SkillResult.NORMAL;
    }
	
}
