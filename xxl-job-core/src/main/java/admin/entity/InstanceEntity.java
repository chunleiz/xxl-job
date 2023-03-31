package admin.entity;

import java.util.UUID;

public class InstanceEntity {
	private UUID requestId = UUID.randomUUID();
	private Object terraformParameters;
	private Object ansibleParameters;
	private String instanceId;
	private String templateId;
	private String templateVersion;
	private String action;
	private String firstExecuted;
	private String privateKey;
	private String routingKey;
	private String sshPort;
	private Long leaseId;
	//rabbitmq routingkey for process message
	private String progressRoutingKey;
	
	public String getProgressRoutingKey() {
		return progressRoutingKey;
	}

	public void setProgressRoutingKey(String progressRoutingKey) {
		this.progressRoutingKey = progressRoutingKey;
	}

	public Long getLeaseId() {
		return leaseId;
	}

	public void setLeaseId(Long leaseId) {
		this.leaseId = leaseId;
	}

	public String getSshPort() {
		return sshPort;
	}

	public void setSshPort(String sshPort) {
		this.sshPort = sshPort;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public UUID getRequestId() {
		return requestId;
	}

	public void setRequestId(UUID requestId) {
		this.requestId = requestId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Object getTerraformParameters() {
		return terraformParameters;
	}

	public void setTerraformParameters(Object terraformParameters) {
		this.terraformParameters = terraformParameters;
	}

	public Object getAnsibleParameters() {
		return ansibleParameters;
	}

	public void setAnsibleParameters(Object ansibleParameters) {
		this.ansibleParameters = ansibleParameters;
	}

	public String getFirstExecuted() {
		return firstExecuted;
	}

	public void setFirstExecuted(String firstExecuted) {
		this.firstExecuted = firstExecuted;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateVersion() {
		return templateVersion;
	}

	public void setTemplateVersion(String templateVersion) {
		this.templateVersion = templateVersion;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

}
