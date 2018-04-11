package com.ailikes.util;

/**
 * 
 * 功能描述: 环境变量配置
 * 
 * @author 徐大伟
 */
public class Configuration {
	
	/**
	 * 贷款接口地址
	 */
	private String loanApiUrl = "";
    /**
     * 会话
     */
    private int cacheYncsid = 1;
    /**
     * 票据
     */
    private int cacheTicket = 1;
    /**
     * 票据验证服务器
     */
    private String ticketUrl = "";
    /**
     * 域名
     */
    private String serverDomain="";
    /**
     * 首页默认地址
     */
    private String yncMasterUrl="";
    
    public String getServerDomain() {
        return serverDomain;
    }

    public void setServerDomain(String serverDomain) {
        this.serverDomain = serverDomain;
    }

    public String getYncMasterUrl() {
        return yncMasterUrl;
    }

    public void setYncMasterUrl(String yncMasterUrl) {
        this.yncMasterUrl = yncMasterUrl;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public int getCacheYncsid() {
        return cacheYncsid;
    }

    public void setCacheYncsid(int cacheYncsid) {
        this.cacheYncsid = cacheYncsid;
    }

    public int getCacheTicket() {
        return cacheTicket;
    }

    public void setCacheTicket(int cacheTicket) {
        this.cacheTicket = cacheTicket;
    }

	public String getLoanApiUrl() {
		return loanApiUrl;
	}

	public void setLoanApiUrl(String loanApiUrl) {
		this.loanApiUrl = loanApiUrl;
	}

}
