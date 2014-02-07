package me.G4meM0ment.GSkills;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.api.events.WeaponDamageEvent;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.effects.EffectType;
import com.herocraftonline.heroes.characters.effects.ExpirableEffect;
import com.herocraftonline.heroes.characters.skill.ActiveSkill;
import com.herocraftonline.heroes.characters.skill.Skill;
import com.herocraftonline.heroes.characters.skill.SkillConfigManager;
import com.herocraftonline.heroes.characters.skill.SkillSetting;
import com.herocraftonline.heroes.characters.skill.SkillType;

public class SkillEmpower extends ActiveSkill {
   
    private String applyText;
    private String expireText;
    private int damage;
	
	public SkillEmpower(Heroes plugin) 
	{
        super(plugin, "Empower");
        setUsage("/skill empower");
        setDescription("You'll went insane and deal $1% damage more.");
        setTypes(SkillType.BUFF);
        setArgumentRange(0, 0);
        setIdentifiers("skill empower");
        Bukkit.getServer().getPluginManager().registerEvents(new SkillEntityListener(this), plugin);
    }

	@Override
	public String getDescription(Hero hero) 
	{
		long duration = (long) (SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION.node(), 8000, false));
        int damage = (int) (SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE.node(), 2, false) *
        		SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE_INCREASE.node(), 0.8, false));
        
        return getDescription().replace("$1", duration+"").replace("§2", damage+"");
	}
	
    @Override
    public ConfigurationSection getDefaultConfig()
    {
    	ConfigurationSection localConfigurationNode = super.getDefaultConfig();
    	localConfigurationNode.set(SkillSetting.DURATION.node(), 8000);
    	localConfigurationNode.set(SkillSetting.DAMAGE.node(), 2);
    	localConfigurationNode.set(SkillSetting.DAMAGE_INCREASE.node(), 0.8);
    	localConfigurationNode.set(SkillSetting.APPLY_TEXT.node(), "%hero% has went insane!");
    	localConfigurationNode.set(SkillSetting.EXPIRE_TEXT.node(), "%hero% calm down!");
    	return localConfigurationNode;
  	}
	
    @Override
    public void init()
    {
    	super.init();
    	this.applyText = SkillConfigManager.getUseSetting(null, this, SkillSetting.APPLY_TEXT.node(), "%target% is absorbing damage!").replace("%target%", "$1");
    	this.expireText = SkillConfigManager.getUseSetting(null, this, SkillSetting.EXPIRE_TEXT.node(), "InvertDamage faded from %target%!").replace("%target%", "$1");
    }
    
    @Override
	public SkillResult use(Hero hero, String[] arg1) 
    {
        long duration = (long) (SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION.node(), 8000, false));
        
        damage = (int) (SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE.node(), 2, false) *
        		SkillConfigManager.getUseSetting(hero, this, SkillSetting.DAMAGE_INCREASE.node(), 0.8, false));
        int stamina = (SkillConfigManager.getUseSetting(hero, this, "Stamina", 16, false));
        
        broadcastExecuteText(hero);
        hero.addEffect(new EmpowerEffect(this, duration, damage, stamina));
        return SkillResult.NORMAL;
	}

    public class SkillEntityListener implements Listener
    {
    	
    	private SkillEmpower se;
    	
    	public SkillEntityListener(SkillEmpower se)
    	{
    		this.se = se;
    	}

    	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    	public void onWeaponDamage(WeaponDamageEvent event) 
    	{
    		Entity d = event.getDamager().getEntity();
    		if(d instanceof Player) 
    		{
    			Player p = (Player) d;
    			Hero h = plugin.getCharacterManager().getHero(p);
    			if(h.hasEffect("Empower"))
    				event.setDamage(event.getDamage()+se.damage);
    		}
    	}
    }
    
    public class EmpowerEffect extends ExpirableEffect
    {
    	private int stamina;
    	
    	public EmpowerEffect(Skill skill, long duration, int damage, int stamina)
    	{
    		super(skill, "Empower", duration);
        	this.types.add(EffectType.WOUNDING);
        	this.stamina = stamina;
    	}

    	@Override
    	public void applyToHero(Hero hero)
    	{
    		super.applyToHero(hero);
    		Player player = hero.getPlayer();
    		broadcast(player.getLocation(), applyText, hero.getPlayer().getDisplayName());
    	}

    	@Override
    	public void removeFromHero(Hero hero)
    	{
    		super.removeFromHero(hero);
    		Player player = hero.getPlayer();
    		broadcast(player.getLocation(), expireText, hero.getPlayer().getDisplayName());
    		
    		if(hero.getPlayer().getFoodLevel() - stamina < 1)
        		hero.getPlayer().setFoodLevel(0);
    		else
    			hero.getPlayer().setFoodLevel(hero.getPlayer().getFoodLevel() - stamina);
    	}
    }    
}


