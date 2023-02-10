package mpti.domain.trainer.application;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import mpti.common.exception.EmailDuplicateException;
import mpti.domain.trainer.dao.TrainerRepository;
import mpti.domain.trainer.dto.TokenDto;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrainerAuthService {
    private final TrainerRepository trainerRepository;

    private final Gson gson;
    private OkHttpClient client = new OkHttpClient();

    @Value("${app.auth.authServerUrl}")
    private String SERVER_URL;

    @Value("${app.auth.userServerUrl}")
    private String USER_URL;

    public boolean isValidDB(String refreshToken) {

        TokenDto tokenDto = new TokenDto();
        tokenDto.setRefreshToken(refreshToken);
        tokenDto.setState(false);

        String json = gson.toJson(tokenDto);

        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(SERVER_URL + "/token")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()){
                String st = response.body().string();
                tokenDto = gson.fromJson(st, TokenDto.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tokenDto.getState();
    }

    public TokenDto getNewAccessToken(String refreshToken) {

        TokenDto tokenDto = new TokenDto();
        tokenDto.setRefreshToken(refreshToken);
        tokenDto.setState(false);

        String json = gson.toJson(tokenDto);

        RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(SERVER_URL + "/token")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()){
                String st = response.body().string();
                tokenDto = gson.fromJson(st, TokenDto.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tokenDto;
    }
}