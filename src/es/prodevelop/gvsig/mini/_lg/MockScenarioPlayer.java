package es.prodevelop.gvsig.mini._lg;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class MockScenarioPlayer {
	
	private static final String TAG = MockScenarioPlayer.class.getSimpleName();
	private AreaManager am;
	private Timer chronos = new Timer();
	private static MockScenarioPlayer instance = new MockScenarioPlayer();
	
	public static MockScenarioPlayer getInstance(){
		return instance;
	}
	
	public void play(List<Action> scenarios, boolean loop){
		Log.d(TAG, "Starting the scenario");
		chronos.scheduleAtFixedRate(new ScenarioTask(scenarios, chronos, loop), 0, 2000);
	}
	
	public void stop(List<Action> scenarios){		
		chronos.cancel();
	}
	
	public static class ScenarioTask extends TimerTask {
		// private List<Action> actions;
		private Action[] actionsA;		
		private int cpt;
		private final Timer chronos;
		private final boolean loop;
		public ScenarioTask(List<Action> scenario, Timer chronos, boolean loop) {
			this.chronos = chronos;
			this.loop = loop;
			if(scenario == null){
				scenario = new ArrayList<Action>(); // will be cancelled 
			}
			actionsA = scenario.toArray(new Action[0]);
			cpt = 0;
		}
		
		public void run() {
			if (cpt > actionsA.length){
				if(!loop){
					chronos.cancel();
					return;
				}
				cpt = 0;
			}
			actionsA[cpt++].execute();
		}		
	}
	
	public static interface Action {
		void execute(); 
	}
	
	public static abstract class LgAction implements Action {
		protected AreaManager _am;
		protected Area _area; 
		public LgAction(AreaManager am, Area a) {
			_am = am;
			_area = a;
		}		
	}
	
	public static class AreaVisitedAction extends LgAction {
		public AreaVisitedAction(AreaManager am, Area a) {
			super(am, a);
		}
		public void execute() {
			_am.fireAreaVisited(_area);
		}		
	}
	
	public static class AreaUnvisitedAction extends LgAction {		
		public AreaUnvisitedAction(AreaManager am, Area a) {
			super(am, a);
		}
		public void execute() {
			_am.fireAreaUnvisited(_area);
		}		
	}
	
	public static class AreaChangedAction extends LgAction {
		private int _event; 
		public AreaChangedAction(AreaManager am, Area a, int event) {
			super(am, a);
			_event = event;
		}
		public void execute() {
			_am.fireAreaChange(_area, _event);
		}		
	}
	
	
	public static abstract class LgTask extends TimerTask {
		protected AreaManager _am;
		protected Area _area; 
		public LgTask(AreaManager am, Area a) {
			_am = am;
			_area = a;
		}		
	}
	
	public static class AreaVisitedTask extends LgTask {
		public AreaVisitedTask(AreaManager am, Area a) {
			super(am, a);
		}
		public void run() {
			_am.fireAreaVisited(_area);
		}		
	}
	
	public static class AreaUnvisitedTask extends LgTask {		
		public AreaUnvisitedTask(AreaManager am, Area a) {
			super(am, a);
		}
		public void run() {
			_am.fireAreaUnvisited(_area);
		}		
	}
	
	public static class AreaChangedTask extends LgTask {
		private int _event; 
		public AreaChangedTask(AreaManager am, Area a, int event) {
			super(am, a);
			_event = event;
		}
		public void run() {
			_am.fireAreaChange(_area, _event);
		}		
	}
	
}
