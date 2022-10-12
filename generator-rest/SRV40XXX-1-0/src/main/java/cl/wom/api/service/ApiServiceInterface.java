package cl.wom.api.service;

import cl.wom.api.controller.response.GetResponse;
import cl.wom.api.controller.response.PostResponse;

public interface ApiServiceInterface {

	public GetResponse getProcess();

	public PostResponse postProcess();

	public String putProcess();

	public void deleteProcess();

}
