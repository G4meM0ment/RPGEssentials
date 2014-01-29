package me.G4meM0ment.GSkills;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.effects.EffectType;
import com.herocraftonline.heroes.characters.effects.PeriodicEffect;
import com.herocraftonline.heroes.characters.skill.PassiveSkill;
import com.herocraftonline.heroes.characters.skill.Skill;
import com.herocraftonline.heroes.characters.skill.SkillConfigManager;
import com.herocraftonline.heroes.characters.skill.SkillSetting;
import com.herocraftonline.heroes.characters.skill.SkillType;

public class SkillGrace extends PassiveSkill {
    
    private String applyText;
    private String expireText;
	
    public SkillGrace(Heroes plugin) 
    {
        super(plugin, "Grace");
        setDescription("Prevents you from damage for $1 seconds after resurrection");
        setTypes(SkillType.BUFF);
        Bukkit.getServer().getPluginManager().registerEvents(new SkillHeroListener(this), plugin);
    }
    
    @Override
    public void init() 
    {
        super.init();
        applyText = SkillConfigManager.getRaw(this, "on-text", ChatColor.GRAY+"Du hast PvP Immunität für $1 Sekunden").replace("$1", ""+getDefaultConfig().getLong(SkillSetting.DURATION.node())/1000);
        expireText = SkillConfigManager.getRaw(this, "off-text", ChatColor.GRAY+"Deine PvP Immunität ist verflogen!");
    }

    @Override
    public String getDescription(Hero hero) 
    {
        long duration = (SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION.node(), 30000, false));
        String description = getDescription().replace("$1", duration/1000 + "");
        return description;
    }

    @Override
    public ConfigurationSection getDefaultConfig() 
    {
        ConfigurationSection node = super.getDefaultConfig();
        node.set(SkillSetting.DURATION.node(), 30000);
        return node;
    }
    
    public SkillResult use(Hero hero, LivingEntity target, String args[]) 
	{
    	hero.addEffect(new GraceEffect(this, (SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION.node(), 30000, false))));
		return SkillResult.NORMAL;
	}
    
    public class GraceEffect extends PeriodicEffect {

        private long period;

        public GraceEffect(SkillGrace skill, long period) 
        {
            super(skill, "Grace", period);
            this.period = period;
            this.types.add(EffectType.UNTARGETABLE);
            this.types.add(EffectType.BENEFICIAL);
        }

        @Override
        public void applyToHero(Hero hero) 
        {
            super.applyToHero(hero);
            Player player = hero.getPlayer();
            player.sendMessage(applyText);
        }

        @Override
        public void removeFromHero(Hero hero) 
        {
            super.removeFromHero(hero);
            Player player = hero.getPlayer();
            player.sendMessage(expireText);
        }

        @Override
        public void tickHero(Hero hero) 
        {
            super.tickHero(hero);

            if(System.currentTimeMillis()-getApplyTime() > period)
	            hero.removeEffect(this);
        }
    }
    
    public class SkillHeroListener implements Listener 
    {
        private Skill skill;
        public SkillHeroListener(Skill skill) 
        {
            this.skill = skill;
        }
        
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onEntityDamageByEntity(EntityDamageByEntityEvent event) 
        {
        	if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
            {
            	Hero heroAsAttacker = skill.plugin.getCharacterManager().getHero((Player) event.getEntity());
            	Hero heroAsAttacked = skill.plugin.getCharacterManager().getHero((Player) event.getDamager());
            	
            	if(heroAsAttacker.hasEffect("Grace"))
            	{
            		((Player) event.getDamager()).sendMessage(ChatColor.GRAY+"Du hast noch PvP Immunität für "+(System.currentTimeMillis()-heroAsAttacked.getEffect("Grace").getApplyTime()/1000)+" Sekunden");
            		event.setCancelled(true);
            	}
            	if(heroAsAttacked.hasEffect("Grace"))
            	{
            		((Player) event.getDamager()).sendMessage(ChatColor.GRAY+"Dieser Spieler hat PvP Immunität für "+(System.currentTimeMillis()-heroAsAttacked.getEffect("Grace").getApplyTime()/1000)+" Sekunden");
            		event.setCancelled(true);
            	}
            }
        }
    }
}
