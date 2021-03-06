package com.openwebstart.extensionpoint;

import com.openwebstart.controlpanel.OpenWebStartControlPanelStyle;
import com.openwebstart.download.ApplicationDownloadIndicator;
import com.openwebstart.jvm.JavaRuntimeManager;
import com.openwebstart.jvm.ui.dialogs.DialogFactory;
import com.openwebstart.jvm.ui.dialogs.RuntimeDownloadDialog;
import com.openwebstart.launcher.JavaRuntimeProvider;
import com.openwebstart.launcher.OwsJvmLauncher;
import com.openwebstart.os.MenuAndDesktopEntryHandler;
import com.openwebstart.proxy.WebStartProxySelector;
import net.adoptopenjdk.icedteaweb.client.controlpanel.ControlPanelStyle;
import net.adoptopenjdk.icedteaweb.client.parts.downloadindicator.DownloadIndicator;
import net.adoptopenjdk.icedteaweb.extensionpoint.ExtensionPoint;
import net.adoptopenjdk.icedteaweb.launch.JvmLauncher;
import net.sourceforge.jnlp.config.DeploymentConfiguration;
import net.sourceforge.jnlp.runtime.MenuAndDesktopIntegration;

import java.net.ProxySelector;
import java.util.Collections;
import java.util.List;

/**
 * Extension point providing OWS specific implementations.
 */
public class OwsExtensionPoint implements ExtensionPoint {

    @Override
    public JvmLauncher createJvmLauncher(final DeploymentConfiguration configuration) {
        final JavaRuntimeProvider javaRuntimeProvider = JavaRuntimeManager.getJavaRuntimeProvider(
                RuntimeDownloadDialog::showDownloadDialog,
                DialogFactory::askForRuntimeUpdate
        );

        return new OwsJvmLauncher(javaRuntimeProvider);
    }

    @Override
    public DownloadIndicator createDownloadIndicator(final DeploymentConfiguration configuration) {
        return new ApplicationDownloadIndicator();
    }

    @Override
    public MenuAndDesktopIntegration createMenuAndDesktopIntegration(final DeploymentConfiguration configuration) {
        return new MenuAndDesktopEntryHandler();
    }

    @Override
    public ProxySelector createProxySelector(final DeploymentConfiguration configuration) {
        return new WebStartProxySelector(configuration);
    }

    @Override
    public ControlPanelStyle createControlPanelStyle(final DeploymentConfiguration configuration) {
        return new OpenWebStartControlPanelStyle();
    }

    @Override
    public List<String> getTranslationResources() {
        return Collections.singletonList("i18n");
    }
}
