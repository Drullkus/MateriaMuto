/**
 * This file is part of MateriaMuto, licensed under the MIT License (MIT).
 *
 * Copyright (c) AgileMods <http://www.agilemods.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.agilemods.materiamuto;

import com.agilemods.materiamuto.common.core.MMBlocks;
import com.agilemods.materiamuto.common.core.MMEntities;
import com.agilemods.materiamuto.common.item.MMItems;
import com.agilemods.materiamuto.common.network.GuiHandler;
import com.agilemods.materiamuto.common.network.packet.PacketHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import com.agilemods.materiamuto.common.core.handlers.AlchemicalBagHandler;
import com.agilemods.materiamuto.common.emc.EMCRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        MMBlocks.init();
        MMItems.init();
        MMEntities.init();

        PacketHandler.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(MateriaMuto.instance, new GuiHandler());

        AlchemicalBagHandler.register();
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {
        EMCRegistry.initialize();
    }
}