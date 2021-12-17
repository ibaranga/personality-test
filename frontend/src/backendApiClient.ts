import { BackendApiClient } from "./backendapi/BackendApiClient";
import config from "./config";

export default new BackendApiClient(config.backendUrl);
