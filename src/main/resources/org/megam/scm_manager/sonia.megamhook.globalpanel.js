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


Ext.ns('Sonia.megamhook');


Sonia.megamhook.GlobalPanel = Ext.extend(Sonia.config.ConfigForm, {
  
  megamhookStore: null,
  
  initComponent: function(){
    this.megamhookStore = new Sonia.rest.JsonStore({
      proxy: new Ext.data.HttpProxy({
        url: restUrl + 'plugins/megamhook.json',
        disableCaching: false
      }),
      root: 'megamhook',
      fields: ['email', 'apikey', 'appname', 'sendCommitData', 'executeOnEveryCommit']
    });
    
    var megamhookColModel = Sonia.megamhook.createColModel();
    var selectionModel = Sonia.megamhook.createRowSelectionModel();
    
    var config = {
      title: Sonia.megamhook.I18n.formTitleText,
      items: [{
        id: 'megamhookGlobalGrid',
        xtype: 'editorgrid',
        clicksToEdit: 1,
        autoExpandColumn: 'uri',
        frame: true,
        width: '100%',
        autoHeight: true,
        autoScroll: false,
        colModel: megamhookColModel,
        sm: selectionModel,
        store: this.megamhookStore,
        viewConfig: {
          forceFit:true
        },
        tbar: [{
          text: Sonia.megamhook.I18n.addText,
          scope: this,
          icon: Sonia.megamhook.I18n.addIcon,
          handler : function(){
            var MegamHook = this.megamhookStore.recordType;
            var p = new MegamHook();
            var grid = Ext.getCmp('megamhookGlobalGrid');
            grid.stopEditing();
            this.megamhookStore.insert(0, p);
            grid.startEditing(0, 0);
          }
        },{
          text: Sonia.megamhook.I18n.removeText,
          scope: this,
          icon: Sonia.megamhook.I18n.removeIcon,
          handler: function(){
            var grid = Ext.getCmp('megamhookGlobalGrid');
            var selected = grid.getSelectionModel().getSelected();
            if ( selected ){
              this.megamhookStore.remove(selected);
            }
          }
        }, '->',{
          id: 'megamhookGlobalGridHelp',
          xtype: 'box',
          autoEl: {
            tag: 'img',
            src: Sonia.megamhook.I18n.helpIcon
          }
        }]
      }]
    };
      
    Ext.apply(this, Ext.apply(this.initialConfig, config));
    Sonia.megamhook.GlobalPanel.superclass.initComponent.apply(this, arguments);
  },
  
  afterRender: function(){
    // call super
    Sonia.megamhook.RepositoryPanel.superclass.afterRender.apply(this, arguments);

    Ext.QuickTips.register({
      target: Ext.getCmp('megamhookGlobalGridHelp'),
      title: '',
      text: Sonia.megamhook.I18n.megamhookGridHelpText,
      enabled: true
    });
  },
  
  onSubmit: function(values){
    this.el.mask(this.submitText);
    values.megamhook = [];
    this.megamhookStore.data.each(function(megamhook){
      values.megamhook.push(megamhook.data);
    });
    Ext.Ajax.request({
      url: restUrl + 'plugins/megamhook.json',
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

Ext.reg("megamhookGlobalPanel", Sonia.megamhook.GlobalPanel);
