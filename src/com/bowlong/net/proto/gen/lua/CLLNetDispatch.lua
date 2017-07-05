--- 网络下行数据调度器
do

  local lizGet = Localization.Get;

  require('GboGtSvBuilder');
  require('GboGmSvBuilder');
  require('BuilderGBaoSng');
  require('CLLData');
  require('CLLDataProc');

  cllNetDis = {
    dispatchSend = function ( map )
      CLLDataProc.procData(map);
    end;

    dispatchHttp = function ( map )
      if(map == nil) then return end;
      BuilderGBaoSng.onCallNet.disp(map);
    end;

    dispatchGate = function ( map )
      if(map == nil) then return end;
      GboGtSvBuilder.onCallNet.disp(map);
    end;

    dispatchGame = function ( map )
      if(map == nil) then return end;
      GboGmSvBuilder.onCallNet.disp(map);
    end;

    dispatch = function ( ... )
      local paras = {...};
      local len = table.getn(paras);

      local cmd = paras[1];  -- 接口名
      local retvalue = paras[len];  -- 接口返回结果

      local data = ArrayList();
      for i=2,len - 1 do
        data:Add(paras[i]);  -- 取得返回数据
      end;

      -- 解密bio
      retvalue.succ = NumEx.bio2Int(retvalue.succ);
      if(retvalue.succ ~= 0) then
        local succ = retvalue.succ;
        retvalue.msg = Localization.Get("Error_" .. succ);
        NAlertTxt.add(retvalue.msg, Color.red, 1);
      else -- success
        cllNetDis.cacheData(cmd, data);
      end;

      -- CLPanelManager.topPanel:onNetwork (cmd, retvalue.succ, nil);
      -- 因为c#调用时已经放在主线程里了，因此可以直接调用procNetwork
      -- CLPanelManager.topPanel:procNetwork (cmd, retvalue.succ, retvalue.msg, data);

      -- 通知所有显示的页面
      if(CLPanelManager.panelRetainLayer ~= nil and CLPanelManager.panelRetainLayer.Count > 0) then
        local showingPanels = CLPanelManager.panelRetainLayer:ToArray();
        for i=0,showingPanels.Length-1 do
          showingPanels[i]:procNetwork (cmd, retvalue.succ, retvalue.msg, data);
        end;
        showingPanels = nil;
      else
        if(CLPanelManager.topPanel ~= nil) then
          CLPanelManager.topPanel:procNetwork (cmd, retvalue.succ, retvalue.msg, data);
        end;
      end;
    end;

    cacheData = function(cmd, data)
      if(cmd == "getNotices") then  -- 公告
        cllNetDis.showGateNotice(data);
      elseif cmd == "entryGame" then
        CLLData.initData();
        CLLData.setSessionId(data[0].val);
        CLLData.setPl(data[1]);
        local tmplist = nil;
        if data[2] ~= nil then
          tmplist = (data[2]).list;
          if tmplist ~= nil then
            CLDBHero.setHeros(tmplist);
          end;
        end;
        if data[3] ~= nil then
          tmplist = (data[3]).list;
          if tmplist ~= nil then
            CLDBNpc.setNpcs(tmplist);
          end;
        end;
        if data[4] ~= nil then
          tmplist = (data[4]).list;
          if tmplist ~= nil then
            CLDBProp.setProps(tmplist);
          end;
        end;
        if data[5] ~= nil then
          tmplist = (data[5]).list;
          if tmplist ~= nil then
            CLDBPart.setPartners(tmplist);
          end;
        end;
      elseif cmd == "endPve" then
        if data[2] ~= nil then
          CLDBProp.setProps((data[2]).list);
        end
        if data[3] ~= nil then
          CLDBNpc.setNpc(data[3]);
        end;
        if data[4] ~= nil then
          CLLData.setPl(data[4]);
        end;
        if data[5] ~= nil then
          CLDBHero.setHero(data[5]);
        end;
      elseif cmd == "upPartnerLev" then
        if data[0] ~= nil then
          local tmpnints = (data[0]).list;
          if tmpnints ~= nil then
            CLDBPart.rmvPartnersByHcids(tmpnints);
          end;
        end;
      elseif cmd == "usePropInBag" then
        if data[0] ~= nil then
          CLDBProp.setProps((data[0]).list);
        end
        if data[1] ~= nil then
          CLLData.setPl(data[1]);
        end;
        if data[2] ~= nil then
          CLDBHero.setHero(data[2]);
        end;
      elseif cmd == "upHeroSklLev" then
        if data[0] ~= nil then
          CLDBHero.setHero(data[0]);
        end;
        if data[1] ~= nil then
          CLLData.setPl(data[1]);
        end;
      elseif cmd == "upEquipGrade" then
        if data[0] ~= nil then
          CLDBProp.setProps((data[0]).list);
        end;
        if data[1] ~= nil then
          CLDBHero.setHero(data[1]);
        end;
        if data[2] ~= nil then
          CLLData.setPl(data[2]);
        end;
      elseif cmd == "upMainHeroLev" then
        CLDBHero.setHero(data[0]);
      elseif cmd == "upPartSklLev1" or cmd == "upPartSklLev2" then
        if data ~= nil and data.Count > 0 then
          CLDBPart.setPartner(data[0]);
          CLDBProp.setProp(data[1]);
        end;
      elseif cmd == "buyPveBuff" then
        CLDBProp.setProp(data[0]);
        CLLData.setPl(data[1]);
      elseif cmd == "notifyRanks" then
        if data[0] ~= nil then
          local tmplist = (data[0]).list;
          if tmplist ~= nil then
            CLLData.setRanks(tmplist);
          end;
        end;
      elseif cmd == "getLoginGift" then
        if data[0] ~= nil then
          CLDBProp.setProps((data[0]).list);
        end;
        if data[1] ~= nil then
          CLDBPart.setPartners((data[1]).list);
        end;
      elseif cmd == "notifyHero" or
        cmd == "addRole" then
        CLDBHero.setHero(data[0]);
      elseif cmd == "notifyHeros" then
        if data[0] ~= nil and data[0].list ~= nil then
          CLDBHero.setHeros(data[0].list);
        end;
      elseif cmd == "notifyProp" then
        CLDBProp.setProp(data[0]);
      elseif cmd == "notifyProps" then
        if data[0] ~= nil and data[0].list ~= nil then
          CLDBProp.setProps(data[0].list);
        end;
      elseif cmd == "notifyPlayer" then
        CLLData.setPl(data[0]);
      elseif cmd == "notifySvStatus" or cmd == "getSves" then
        CLLData.setServers(data[0].nsves);
      elseif cmd == "notifyPartners" then
        CLDBPart.setPartners(data[0].list);
      elseif cmd == "notifyPartner" then
        CLDBPart.setPartner(data[0]);
      elseif cmd == "sync2Local" then
        DBTools.saveDBLocal(data[0],data[1],data[2],data[3],data[4],data[5]);
      elseif cmd == "notifyEmail" then
        CLDBEmail.initOptDB();
        CLDBEmail.setEmail(data[0]);
      elseif cmd == "notifyEmails" then
        CLDBEmail.initOptDB();
        if data[0] ~= nil and data[0].list ~= nil then
          CLDBEmail.setEmails(data[0].list);
        end;
      elseif cmd == "getNRanks" then
        CLDBRnk.setSelfRnk(data[0]);
        if data[1] ~= nil and data[1].list ~= nil then
          CLDBRnk.setRnks((data[1]).list);
        end;
      elseif cmd == "getNEmals" then
        if data[0] ~= nil and data[0].list ~= nil then
          local tmpList = (data[0]).list;
          DBMail.setMails(tmpList);
          CLDBEmail.setEmails(tmpList);
        end;
      end;
    end;

    -- 显示网关公告
    showGateNotice = function ( paras )
      local data = paras[0];
      if(data ~= nil and data.list ~= nil) then
        local count = data.list.Count;
        local msg = "";
        local notice = nil;
        for i=0, count-1 do
          notice = data.list[i];
          msg = msg .. notice.title .. "\n" .. notice.cont .. "\n";
        end;
        if(msg ~= "") then
          CLUIUtl.showConfirm(msg, nil);
        end;
      end;
    end;

  }

end

module("CLLNetDispatch",package.seeall)
