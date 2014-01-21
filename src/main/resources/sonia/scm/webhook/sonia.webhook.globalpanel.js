/* *
 * Copyright (c) 2010, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of SCM-Manager; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://bitbucket.org/sdorra/scm-manager
 * 
 */


Ext.ns('Sonia.webhook');


Sonia.webhook.GlobalPanel = Ext.extend(Sonia.config.ConfigForm, {
  
  webhookStore: null,
  
  initComponent: function(){
    this.webhookStore = new Sonia.rest.JsonStore({
      proxy: new Ext.data.HttpProxy({
        url: restUrl + 'plugins/webhook.json',
        disableCaching: false
      }),
      root: 'webhook',
      fields: ['urlPattern', 'sendCommitData', 'executeOnEveryCommit']
    });
    
    var webhookColModel = Sonia.webhook.createColModel();
    var selectionModel = Sonia.webhook.createRowSelectionModel();
    
    var config = {
      title: Sonia.webhook.I18n.formTitleText,
      items: [{
        id: 'webhookGlobalGrid',
        xtype: 'editorgrid',
        clicksToEdit: 1,
        autoExpandColumn: 'uri',
        frame: true,
        width: '100%',
        autoHeight: true,
        autoScroll: false,
        colModel: webhookColModel,
        sm: selectionModel,
        store: this.webhookStore,
        viewConfig: {
          forceFit:true
        },
        tbar: [{
          text: Sonia.webhook.I18n.addText,
          scope: this,
          icon: Sonia.webhook.I18n.addIcon,
          handler : function(){
            var WebHook = this.webhookStore.recordType;
            var p = new WebHook();
            var grid = Ext.getCmp('webhookGlobalGrid');
            grid.stopEditing();
            this.webhookStore.insert(0, p);
            grid.startEditing(0, 0);
          }
        },{
          text: Sonia.webhook.I18n.removeText,
          scope: this,
          icon: Sonia.webhook.I18n.removeIcon,
          handler: function(){
            var grid = Ext.getCmp('webhookGlobalGrid');
            var selected = grid.getSelectionModel().getSelected();
            if ( selected ){
              this.webhookStore.remove(selected);
            }
          }
        }, '->',{
          id: 'webhookGlobalGridHelp',
          xtype: 'box',
          autoEl: {
            tag: 'img',
            src: Sonia.webhook.I18n.helpIcon
          }
        }]
      }]
    };
      
    Ext.apply(this, Ext.apply(this.initialConfig, config));
    Sonia.webhook.GlobalPanel.superclass.initComponent.apply(this, arguments);
  },
  
  afterRender: function(){
    // call super
    Sonia.webhook.RepositoryPanel.superclass.afterRender.apply(this, arguments);

    Ext.QuickTips.register({
      target: Ext.getCmp('webhookGlobalGridHelp'),
      title: '',
      text: Sonia.webhook.I18n.webhookGridHelpText,
      enabled: true
    });
  },
  
  onSubmit: function(values){
    this.el.mask(this.submitText);
    values.webhook = [];
    this.webhookStore.data.each(function(webhook){
      values.webhook.push(webhook.data);
    });
    Ext.Ajax.request({
      url: restUrl + 'plugins/webhook.json',
      method: 'POST',
      jsonData: values,
      scope: this,
      disableCaching: true,
      success: function(response){
        this.el.unmask();
      },
      failure: function(){
        this.el.unmask();
      }
    });
  },

  onLoad: function(el){
    // do nothing
  }
  
});

Ext.reg("webhookGlobalPanel", Sonia.webhook.GlobalPanel);