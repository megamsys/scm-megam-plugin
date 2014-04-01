/* *
 * Copyright (c) 2010, rajthilak
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

Sonia.megamhook.RepositoryPanel = Ext.extend(Sonia.repository.PropertiesFormPanel, {
  
  megamhookStore: null,
  
  initComponent: function(){
    this.megamhookStore = new Ext.data.ArrayStore({
      fields: [
        {name: 'email'},
        {apikey: 'apikey'},
        {appname: 'appname'},
        {name: 'executeOnEveryCommit', type: 'boolean'},
        {name: 'sendCommitData', type: 'boolean'}
      ]
    });
    
    this.loadMegamhooks(this.megamhookStore, this.item);
    
    var megamhookColModel = Sonia.megamhook.createColModel();
    
    var selectionModel = Sonia.megamhook.createRowSelectionModel();
    
    var config = {
      title: Sonia.megamhook.I18n.formTitleText,
      items: [{
        id: 'megamhookGrid',
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
            var grid = Ext.getCmp('megamhookGrid');
            grid.stopEditing();
            this.megamhookStore.insert(0, p);
            grid.startEditing(0, 0);
          }
        },{
          text: Sonia.megamhook.I18n.removeText,
          scope: this,
          icon: Sonia.megamhook.I18n.removeIcon,
          handler: function(){
            var grid = Ext.getCmp('megamhookGrid');
            var selected = grid.getSelectionModel().getSelected();
            if ( selected ){
              this.megamhookStore.remove(selected);
            }
          }
        }, '->',{
          id: 'megamhookGridHelp',
          xtype: 'box',
          autoEl: {
            tag: 'img',
            src: Sonia.megamhook.I18n.helpIcon
          }
        }]
      }]
    };
    Ext.apply(this, Ext.apply(this.initialConfig, config));
    Sonia.megamhook.RepositoryPanel.superclass.initComponent.apply(this, arguments);
  },
  
  afterRender: function(){
    // call super
    Sonia.megamhook.RepositoryPanel.superclass.afterRender.apply(this, arguments);

    Ext.QuickTips.register({
      target: Ext.getCmp('megamhookGridHelp'),
      title: '',
      text: Sonia.megamhook.I18n.megamhookGridHelpText,
      enabled: true
    });
  },
 
  loadMegamhooks: function(store, repository){
    if (debug){
      console.debug('load megamhook properties');
    }
    if (!repository.properties){
      repository.properties = [];
    }
    Ext.each(repository.properties, function(prop){
      if ( prop.key === 'megamhooks' ){
        var value = prop.value;
        this.parseMegamhooks(store, value);
      }
    }, this);
  },
  
  parseMegamhooks: function(store, megamhookString){
    var parts = megamhookString.split('|');
    Ext.each(parts, function(part){
      var pa = part.split(';');
      if ( pa.length > 0 && pa[0].length > 0 ){
        var Megamhook = store.recordType;
        var w = new Megamhook({
          email: pa[0].trim(),
          apikey: pa[1].trim(),
          appname: pa[2].trim(),
          executeOnEveryCommit: pa[3] === 'true',
          sendCommitData: pa[4] === 'true'
        });
        if (debug){
          console.debug('add megamhook: ');
          console.debug( w );
        }
        store.add(w);
      }
    });
  },
  
  storeExtraProperties: function(repository){
    if (debug){
      console.debug('store megamhook properties');
    }
    
    // delete old sub repositories
    Ext.each(repository.properties, function(prop, index){
      if ( prop.key === 'megamhooks' ){
        delete repository.properties[index];
      }
    });
    
    var megamhookString = '';
    this.megamhookStore.data.each(function(r){
      var w = r.data;
      // TODO set sendCommitData
      megamhookString += w.email + ';' + w.apikey + ';' + w.appname + ';' + w.executeOnEveryCommit + ';false|';
    });
    
    if (debug){
      console.debug('add megamhook string: ' + megamhookString);
    }
    
    repository.properties.push({
      key: 'megamhooks',
      value: megamhookString
    });
  }
  
  
});


// register xtype
Ext.reg("megamhookRepositoryPanel", Sonia.megamhook.RepositoryPanel);
