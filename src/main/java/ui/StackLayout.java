package main.java.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class StackLayout implements LayoutManager
{
    public final static int HORIZONTAL = 0;
    public final static int VERTICAL = 1;

    public boolean hasMargin = true;
    public boolean centerAligh = false;
    public boolean stretch = false;
    int direction;
    int gap;
    int maxChildWidth = 0, maxChildHeight = 0;

    public StackLayout()
    {
        this(VERTICAL, 10);
    }

    /* Overload */
    public StackLayout(int direction, int gap)
    {
        this(direction, gap, true);
    }

    public StackLayout(int direction, int gap, boolean hasMargin)
    {
        this(direction, gap, hasMargin, false);
    }

    public StackLayout(int direction, int gap, boolean hasMargin,
            boolean centerAlign)
    {
        this(direction, gap, hasMargin, centerAlign, false);
    }

    public StackLayout(int direction, int gap, boolean hasMargin,
            boolean centerAlign, boolean stretch)
    {
        this.direction = direction;
        this.gap = gap;
        this.hasMargin = hasMargin;
        this.centerAligh = centerAlign;
        this.stretch = stretch;
    }

    @Override
    public void addLayoutComponent(String name, Component comp)
    {
    }

    @Override
    public void removeLayoutComponent(Component comp)
    {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent)
    {
        return minimumLayoutSize(parent);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent)
    {
        synchronized (parent.getTreeLock())
        {
            Insets insets = parent.getInsets();
            int width = 0, height = 0, widthSum = 0, heightSum = 0;
            for (Component component : parent.getComponents())
            {
                int cWidth = component.getPreferredSize().width;
                int cHeight = component.getPreferredSize().height;
                widthSum += cWidth;
                heightSum += cHeight;
                if (cWidth > maxChildWidth)
                    maxChildWidth = cWidth;
                if (cHeight > maxChildHeight)
                    maxChildHeight = cHeight;
            }
            int count = parent.getComponentCount();
            switch (direction)
            {
            case HORIZONTAL:
                width = widthSum + (count - 1) * gap;
                height = maxChildHeight;
                break;
            case VERTICAL:
                width = maxChildWidth;
                height = heightSum + (count - 1) * gap;
                break;
            }
            if (hasMargin)
            {
                width += 2 * gap;
                height += 2 * gap;
            }
            return new Dimension(insets.left + insets.right + width,
                    insets.top + insets.bottom + height);
        }
    }

    @Override
    public void layoutContainer(Container parent)
    {
        Insets insets = parent.getInsets();
        int x = insets.left, y = insets.top;
        if (hasMargin)
        {
            x += gap;
            y += gap;
        }
        for (int i = 0; i < parent.getComponentCount(); i++)
        {
            Component child = parent.getComponent(i);
            Dimension childD = child.getPreferredSize();

            switch (direction)
            {
            case HORIZONTAL:
                int offsetY = centerAligh
                        ? (int) (maxChildHeight - childD.getHeight()) / 2 : 0;
                child.setBounds(x, y + offsetY, childD.width,
                        stretch ? maxChildHeight : childD.height);
                x += childD.width + gap;
                break;
            case VERTICAL:
                int offsetX = centerAligh
                        ? (int) (maxChildWidth - childD.getWidth()) / 2 : 0;
                child.setBounds(x + offsetX, y,
                        stretch ? maxChildWidth : childD.width, childD.height);
                y += childD.height + gap;
            }
        }
    }
}
