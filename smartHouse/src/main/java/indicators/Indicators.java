package indicators;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import database.ReadInDatabase;
import database.data.DataRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loulou on 03/02/2017.
 */
public class Indicators {
	private static final Indicators INSTANCE = new Indicators();

	public static Indicators getInstance() {
		return INSTANCE;
	}

	private List<Indicator> indicators = new ArrayList();

	public List<Indicator> getIndicators() {
		return indicators;
	}

	public Indicator getIndicatorByString(String type) {
		Indicator indicatorResult = null;
		for (Indicator indicator : indicators) {
			if (indicator.getType().equals(type)) {
				indicatorResult = indicator;
			}
		}
		return indicatorResult;
	}

	public JsonElement getLastValues() {
		ArrayList<DataRecord> result = ReadInDatabase.getLastIndicatorsValues();
		Gson gson = new Gson();
		JsonElement resultsToJson = gson.toJsonTree(result);
		return resultsToJson;
	}

	public void addIndicator(Indicator indicator) {
		indicators.add(indicator);
	}

	public void removeIndicator(Indicator indicator) {
		indicators.remove(indicator);
	}
}
