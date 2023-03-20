package fr.simplon.clubsportif;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Lance le {@link ClubSportifManager#main(String[])} dans une console externe. Ceci permet de pouvoir effacer le texte
 * d'une commande à l'autre.
 */
public class RunInExternalConsole
{
    public static void main(String[] args)
    {
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        String classPath = bean.getClassPath();
        try
        {
            ProcessBuilder pb = new ProcessBuilder();
            pb.directory(new File(System.getProperty("user.dir")));
            pb.command("cmd.exe", //
                       "/C", //
                       "start", //
                       "java.exe", //
                       "-Dfile.encoding=UTF-8",//
                       "-Dsun.stdout.encoding=UTF-8",//
                       "-Dsun.stderr.encoding=UTF-8", //
                       "-classpath", //
                       classPath, //
                       "fr.simplon.clubsportif.ClubSportifManager");
            Process start = pb.start();
        }
        catch (IOException pE)
        {
            throw new RuntimeException(pE);
        }
    }
}
