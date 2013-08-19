package me.G4meM0ment.GSkills;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.SkillResult;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.effects.EffectType;
import com.herocraftonline.heroes.characters.effects.ExpirableEffect;
import com.herocraftonline.heroes.characters.skill.ActiveSkill;
import com.herocraftonline.heroes.characters.skill.Skill;
import com.herocraftonline.heroes.characters.skill.SkillConfigManager;
import com.herocraftonline.heroes.characters.skill.SkillSetting;
import com.herocraftonline.heroes.characters.skill.SkillType;

public class SkillEmpower extends ActiveSkill{
   
    private String applyText;
    private String expireText;
    private int damage;
	
	public SkillEmpower(Heroes plugin) {
        super(plugin, "Empower");
        setUsage("/skill empower");
        setDescription("You'll went insane and deal $1% damage more.");
        setTypes(SkillType.BUFF, SkillType.MANA);
        setArgumentRange(0, 0);
        setIdentifiers("skill empower");
        Bukkit.getServer().getPluginManager().registerEvents(new SkillEntityListener(this), plugin);
    }

	@Override
	public String getDescription(Hero hero) {
		long duration = (long) (SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION.node(), 8000, false));
        String description = getDescription().replace("$1", duration + "");
        
        //MANA
        int mana = SkillConfigManager.getUseSetting(hero, this, SkillSetting.MANA.node(), 15, false);
        description += " Mana: "+mana;
        
        int baseDamage = (int) (SkillConfigManager.getUseSetting(hero, this, "BaseDamage", 2, false) *
        		SkillConfigManager.getUseSetting(hero, this, "LevelMultiplier", 0.8, false));
        description += " Damage: "+baseDamage;
        
		return description;
	}
	
    @Override
    public ConfigurationSection getDefaultConfig()
    {
    	ConfigurationSection localConfigurationNode = super.getDefaultConfig();
    	localConfigurationNode.set(SkillSetting.DURATION.node(), 8000);
    	localConfigurationNode.set("BaseDamage", 2);
    	localConfigurationNode.set("LevelMultiplier", 0.8);
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
	public SkillResult use(Hero hero, String[] arg1) {
        long duration = (long) (SkillConfigManager.getUseSetting(hero, this, SkillSetting.DURATION.node(), 8000, false));
        int mana = SkillConfigManager.getUseSetting(hero, this, SkillSetting.MANA.node(), 15, false);
        
        damage = (int) (SkillConfigManager.getUseSetting(hero, this, "BaseDamage", 2, false) *
        		SkillConfigManager.getUseSetting(hero, this, "LevelMultiplier", 0.8, false));
        int stamina = (SkillConfigManager.getUseSetting(hero, this, "Stamina", 16, false));
        
        broadcastExecuteText(hero);
        hero.addEffect(new InvertDamageEffect(this, duration, damage, mana, stamina));
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
    	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    		Entity e = event.getEntity();
    		Entity d = event.getDamager();
    		if(d instanceof Player) {
    			Hero h = plugin.getCharacterManager().getHero((Player) e);
    			if(h.hasEffect("Empower")) {
    				event.setDamage(event.getDamage()+se.damage);
    			}
    		}
    	}
    }
    
    public class InvertDamageEffect extends ExpirableEffect
    {
    	
    	private long duration;
    	private int damage;
    	private int mana;
    	private int stamina;
    	
    	public InvertDamageEffect(Skill skill, long duration, int damage, int mana, int stamina)
    	{
    		super(skill, "Empower", duration);
        	this.types.add(EffectType.BENEFICIAL);
        	this.types.add(EffectType.DISPELLABLE);
        	this.duration = duration;
        	this.damage = damage;
        	this.mana = mana;
        	this.stamina = stamina;
    	}

    	@Override
    	public void applyToHero(Hero hero)
    	{
    		super.applyToHero(hero);
    		Player player = hero.getPlayer();
    		broadcast(player.getLocation(), applyText, hero.getPlayer().getDisplayName());
    		if(hero.getMana() > 0 && hero.getMana() - mana < 0) {
    			hero.setMana(0);    			
    		} else if(hero.getMana() - mana > 0) {
    			hero.setMana(hero.getMana()-mana);
    		}
    	}

    	@Override
    	public void removeFromHero(Hero hero)
    	{
    		super.removeFromHero(hero);
    		Player player = hero.getPlayer();
    		broadcast(player.getLocation(), expireText, hero.getPlayer().getDisplayName());
    		if(hero.getPlayer().getFoodLevel() - stamina < 1)
        		hero.getPlayer().setFoodLevel(0);
    		
    		hero.getPlayer().setFoodLevel(hero.getPlayer().getFoodLevel() - stamina);
    	}
    }    
}


