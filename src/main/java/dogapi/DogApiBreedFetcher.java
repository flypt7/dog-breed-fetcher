package dogapi;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        client.newBuilder()
                .build();
        final JSONObject requestBody = new JSONObject();
        requestBody.put("breed", breed);
        // final RequestBody body = RequestBody.create(mediaType, requestBody.toString()); // was used in lab 5, not sure what it does... method used to be "POST" in line 40 with iit being used as the body
        final Request request = new Request.Builder()
                .url(String.format("%s/api/breed/%s/list", "https://dog.ceo", breed))
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getString("status").equals("success")) {
                final JSONArray subBreedsArray = responseBody.getJSONArray("message");
                ArrayList<String> subBreeds = new ArrayList<>();
                for (int i = 0; i < subBreedsArray.length(); i++) {
                    subBreeds.add(subBreedsArray.getString(i));

                }

                return subBreeds;
            }
            else {
                throw new BreedNotFoundException(breed);
            }
        }
        catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }
}