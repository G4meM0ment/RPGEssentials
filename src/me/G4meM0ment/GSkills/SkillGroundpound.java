package me.G4meM0ment.GSkills;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.skill.ActiveSkill;
import com.herocraftonline.heroes.characters.skill.SkillConfigManager;
import com.herocraftonline.heroes.characters.skill.SkillSetting;
import com.herocraftonline.heroes.characters.skill.SkillType;

public class SkillGroundpound extends ActiveSkill {
	
    public SkillGroundpound(Heroes plugin) 
    {
        super(plugin, "Groundpound");
        setUsage("/skill groundpound");
        setDescription("You'll jump and make do aoe damage");
        setTypes(SkillType.DAMAGING, SkillType.FORCE);
        setArgumentRange(0, 0);
        setIdentifiers("skill groundpound");
    }

    @Override
    public String getDescription(Hero hero) 
    {
    	
    	double distance = ((SkillConfigManager.getUseSetting(hero, this, SkillSetting.RADIUS.node(), 10, false)));
    	int baseDamage = (int) ((SkillConfigManager.getUseSetting(hero, this, "BaseDamage", 6, false)) +
    			((SkillConfigManager.getUseSetting(hero, this, "LevelMultiplier", 0.1, false)) * hero.getSkillLevel(this)));
    	int targets = ((SkillConfigManager.getUseSetting(hero, this, "targets", 6, false)));
    	int jumpHight = (int) ((SkillConfigManager.getUseSetting(hero, this, "jumpMultiplier", 1.2, false))) * hero.getSkillLevel(this);
    	
        String description = getDescription().replace("$1", distance + "").replace("$2", baseDamage + "").replace("$3", targets + "").replace("$4", jumpHight + "");
        return description;
    }

    @Override
    public ConfigurationSection getDefaultConfig() 
    {
        ConfigurationSection node = super.getDefaultConfig();
        node.set(SkillSetting.RADIUS.node(), 10);
        node.set("BaseDamage", 6);
        node.set("LevelMultiplier", 0.1);
        node.set("targets", 6);
        node.set("jumpMultiplier", 1.2);
        return node;
    }
    
	@Override
    public SkillResult use(Hero hero, String args[]) 
	{
    	Player p = hero.getPlayer();
    	int radius = SkillConfigManager.getUseSetting(hero, this, SkillSetting.RADIUS.node(), 10, false);
        List<Entity> entites = p.getNearbyEntities(radius, radius, radius);
        int counter = SkillConfigManager.getUseSetting(hero, this, "targets", 6, false);
        double damage = ((SkillConfigManager.getUseSetting(hero, this, "BaseDamage", 6, false)) +
    			((SkillConfigManager.getUseSetting(hero, this, "LevelMultiplier", 0.1, false)) * hero.getSkillLevel(this)));
		Location l = p.getLocation();
		l.setY(l.getY()+((SkillConfigManager.getUseSetting(hero, this, "jumpMultiplier", 1.2, false))));
        
        hero.setMana(hero.getMana() - (SkillConfigManager.getUseSetting(hero, this, "mana", 12, false)));
        
	    for(Entity e : entites) 
	    	if((e instanceof LivingEntity) && counter > 0)
	        {
	    		LivingEntity le = (LivingEntity) e;
	    		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, e, DamageCause.ENTITY_ATTACK, damage);
	    		
	    		Bukkit.getPluginManager().callEvent(event);
	    		if(event.isCancelled())
	    			continue;
	    		
	    		le.damage(event.getDamage());
	    		le.setVelocity(le.getVelocity().setY(SkillConfigManager.getUseSetting(hero, this, "jumpMultiplier", 1.2, false)));
	            counter--;
	        }	    	
        return SkillResult.NORMAL;
    }
	
    public Player getPlayer(final String name) 
    {
        Player[] players = Bukkit.getOnlinePlayers();
 
        Player found = null;
        String lowerName = name.toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (Player player : players) 
        {
            if (player.getName().toLowerCase().startsWith(lowerName)) 
            {
                int curDelta = player.getName().length() - lowerName.length();
                if (curDelta < delta) 
                {
                    found = player;
                    delta = curDelta;
                    break;
                }
                if (curDelta == 0) break;
            }

        }
        return found;
    }	
}
